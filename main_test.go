package main

import (
	"bytes"
	"context"
	"crypto/md5"
	"fmt"
	"strconv"
	"testing"
)

const defaultTestAddr = "http://example.com"

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
		{
			"https://c",
			"https://c",
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

func TestProcessMany(t *testing.T) {
	const count = 100

	var out bytes.Buffer
	var addrs []string
	var expected string

	for i := 0; i < count; i++ {
		expected = fmt.Sprintf("%s%s %x\n", expected, defaultTestAddr, md5.Sum([]byte(defaultTestAddr)))
		addrs = append(addrs, defaultTestAddr)
	}

	fetch := func(string) ([]byte, error) {
		return []byte(defaultTestAddr), nil
	}

	process(context.Background(), fetch, &out, addrs, count)

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
