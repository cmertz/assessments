package main

import (
	"bytes"
	"context"
	"crypto/md5"
	"fmt"
	"strconv"
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
	if out.String() != expected {
		t.Errorf("expected %s, got %s\n", expected, out.String())
	}
}
