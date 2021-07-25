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

func testFetch(addr string) ([]byte, error) {
	return []byte(addr), nil
}

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
		c := c

		t.Run(strconv.Itoa(i), func(t *testing.T) {
			r := addScheme([]string{c.addr})

			if r[0] != c.addrWithScheme {
				t.Errorf("expected %s, got %s\n", c.addrWithScheme, r[0])
			}
		})
	}
}

func TestProcessSingle(t *testing.T) {
	var out bytes.Buffer

	process(context.Background(), testFetch, &out, []string{defaultTestAddr}, 1)

	expected := fmt.Sprintf("%s %x\n", defaultTestAddr, md5.Sum([]byte(defaultTestAddr)))
	actual := out.String()

	if actual != expected {
		t.Errorf("expected %s, got %s\n", expected, actual)
	}
}

func TestProcessMany(t *testing.T) {
	const count = 100

	var addrs []string
	var expected string

	for i := 0; i < count; i++ {
		expected = fmt.Sprintf("%s%s %x\n", expected, defaultTestAddr, md5.Sum([]byte(defaultTestAddr)))
		addrs = append(addrs, defaultTestAddr)
	}

	var out bytes.Buffer

	process(context.Background(), testFetch, &out, addrs, count)

	actual := out.String()
	if actual != expected {
		t.Errorf("expected %s, got %s\n", expected, actual)
	}
}

func TestProcessCancelation(t *testing.T) {
	var out bytes.Buffer

	latch := make(chan struct{})
	done := make(chan struct{})

	fetch := func(addr string) ([]byte, error) {
		latch <- struct{}{}
		<-latch

		return testFetch(addr)
	}

	ctx, cancel := context.WithCancel(context.Background())

	go func() {
		process(ctx, fetch, &out, []string{defaultTestAddr, defaultTestAddr}, 1)
		done <- struct{}{}
	}()

	<-latch
	cancel()
	latch <- struct{}{}
	<-done

	actual := out.String()
	expected := fmt.Sprintf("%s %x\n", defaultTestAddr, md5.Sum([]byte(defaultTestAddr)))

	if actual != expected {
		t.Errorf("expected %s, got %s\n", expected, actual)
	}
}
