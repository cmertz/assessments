package main

import (
	"flag"
	"fmt"
	"log"
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
	var parallel int
	flag.IntVar(&parallel, "parallel", 10, "number of concurrent http requests")
	flag.Parse()

	if parallel < 0 {
		log.Fatalf("negative number of concurrent http requests given (%d)\n", parallel)
	}

	// TODO REMOVEME
	fmt.Println(addScheme(flag.Args()))
}
