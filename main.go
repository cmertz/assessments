package main

import (
	"context"
	"flag"
	"fmt"
	"log"
	"os/signal"
	"strings"
	"sync"
	"syscall"
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

	// this should not really matter given the max number of addresses we can pass
	// as command line args. For the sake of completeness we add it anyways.
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGQUIT, syscall.SIGTERM)
	defer stop()

	go func() {
	outer:
		for _, addr := range addScheme(flag.Args()) {
			select {
			case addrs <- addr:
			case <-ctx.Done():
				break outer
			}
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
