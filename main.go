package main

import (
	"flag"
	"fmt"
	"log"
	"strings"
	"sync"
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

	var wg sync.WaitGroup
	wg.Add(parallel)

	addrs := make(chan string)

	go func() {
		for _, addr := range addScheme(flag.Args()) {
			addrs <- addr
		}

		close(addrs)
	}()

	for i := 0; i < parallel; i++ {
		go func() {
			for addr := range addrs {
				fmt.Println(addr)
			}

			wg.Done()
		}()
	}

	wg.Wait()
}
