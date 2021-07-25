package main

import (
	"bytes"
	"context"
	"crypto/md5"
	"fmt"
	"strconv"
	"sync/atomic"
	"testing"
)

func TestAddScheme(t *testing.T) {
	cases := []struct {
		addr           string
		addrWithScheme string
	}{
		{
			"http://a",
			"http://a",
		},
		{
			"b",
			"http://b",
		},
	}

	for i, c := range cases {
		t.Run(strconv.Itoa(i), func(t *testing.T) {
			r := addScheme([]string{c.addr})

			if r[0] != c.addrWithScheme {
				t.Errorf("expected %s, got %s\n", c.addrWithScheme, r[0])
			}
		})
	}
}

func TestProcess(t *testing.T) {
	const testAddr = "http://example.com"

	var out bytes.Buffer

	process(context.Background(), func(string) ([]byte, error) { return []byte(testAddr), nil }, &out, []string{testAddr}, 1)

	expected := fmt.Sprintf("%s %x\n", testAddr, md5.Sum([]byte(testAddr)))
	actual := out.String()
	if actual != expected {
		t.Errorf("expected %s, got %s\n", expected, actual)
	}
}

func TestProcessParallel(t *testing.T) {
	const testAddr = "http://example.com"
	const parallel = 100

	var numberOfFetchCalls uint32
	fetch := func(string) ([]byte, error) {
		atomic.AddUint32(&numberOfFetchCalls, 1)
		return []byte(testAddr), nil
	}

	var out bytes.Buffer
	var in []string
	var expected string

	for i := 0; i < parallel; i++ {
		expected = fmt.Sprintf("%s%s %x\n", expected, testAddr, md5.Sum([]byte(testAddr)))
		in = append(in, testAddr)
	}

	process(context.Background(), fetch, &out, in, parallel)

	if numberOfFetchCalls != parallel {
		t.Errorf("expected %d calls to fetch function, got %d\n", parallel, numberOfFetchCalls)
	}

	actual := out.String()
	if actual != expected {
		t.Errorf("expected %s, got %s\n", expected, actual)
	}
}

func TestProcessCancelation(t *testing.T) {
	const testAddr = "http://example.com"

	var out bytes.Buffer

	latch := make(chan struct{})
	done := make(chan struct{})

	fetch := func(string) ([]byte, error) {
		latch <- struct{}{}
		<-latch
		return []byte(testAddr), nil
	}

	ctx, cancel := context.WithCancel(context.Background())
	go func() {
		process(ctx, fetch, &out, []string{testAddr, testAddr}, 1)
		done <- struct{}{}
	}()

	<-latch
	cancel()
	latch <- struct{}{}

	<-done

	actual := out.String()
	expected := fmt.Sprintf("%s %x\n", testAddr, md5.Sum([]byte(testAddr)))

	if actual != expected {
		t.Errorf("expected %s, got %s\n", expected, actual)
	}
}
