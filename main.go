package main

import (
	"flag"
	"fmt"
	"strings"
)

func addScheme(addrs []string) []string {
	var res []string

	for _, a := range addrs {
		if !strings.HasPrefix(a, "http://") {
			a = fmt.Sprintf("http://%s", a)
		}

		res = append(res, a)
	}

	return res
}

func main() {
	var parallel uint
	flag.UintVar(&parallel, "parallel", 10, "number of concurrent http requests")
	flag.Parse()

	fmt.Println(addScheme(flag.Args()))
}
