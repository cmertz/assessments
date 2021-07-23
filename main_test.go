package main

import (
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
