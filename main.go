package main

import (
	"context"
	"crypto/md5"
	"flag"
	"fmt"
	"io"
	"log"
	"os"
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

type fetchFunc func(addr string) ([]byte, error)

func fetch(addr string) ([]byte, error) {
	return nil, nil
}

func process(ctx context.Context, fetch fetchFunc, out io.Writer, addresses []string, parallel int) {
	var wg sync.WaitGroup
	wg.Add(parallel)

	addrs := make(chan string)
	results := make(chan string)

	go func() {
	outer:
		for _, addr := range addresses {
			select {
			case addrs <- addr:
			case <-ctx.Done():

				// this should properly propagate any "cancelation signal"
				// to the fetching goroutines via the closing of the channel
				// below
				break outer
			}
		}

		close(addrs)
	}()

	for i := 0; i < parallel; i++ {
		go func() {
			for addr := range addrs {
				content, err := fetch(addr)
				if err != nil {
					results <- fmt.Sprintf("%s %s", addr, err)
				} else {
					results <- fmt.Sprintf("%s %x", addr, md5.Sum([]byte(content)))
				}
			}

			wg.Done()
		}()
	}

	go func() {
		for res := range results {
			_, err := fmt.Fprint(out, res)
			if err != nil {

				// there is no point in continuing if we can't
				// output the results
				panic(fmt.Sprintf("error writing results: %s\n", err))
			}
		}
	}()

	wg.Wait()

	// the fetching goroutines should be done writing to the channel
	// at this point, closing it ends the printing goroutine
	close(results)
}

func main() {
	var parallel int
	flag.IntVar(&parallel, "parallel", 10, "number of concurrent http requests")
	flag.Parse()

	if parallel < 0 {
		log.Fatalf("negative number of concurrent http requests given (%d)\n", parallel)
	}

	// this should not really matter given the max number of addresses we can pass
	// as command line args. For the sake of completeness we add it anyways.
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGQUIT, syscall.SIGTERM)
	defer stop()

	process(ctx, fetch, os.Stdout, addScheme(flag.Args()), parallel)
}
