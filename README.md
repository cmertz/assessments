# myhttp

**This is a code for a coding challenge**

Command line tool to fetch websites and output the md5 digest of their content.

To speed things up it allows for fetching multiple sites at once, with a specifiable number of parallel requests.

## usage

Flags:
```
-parallel int
    number of parallel http requests (default 10)
```

Examples:

```
$ ./myhttp google.com
http://google.com 9afc9c0eace822950fe018268c1f2450

$ ./myhttp http://google.com
http://google.com 363b2221caff08a90a46cb4c75613a26

$ ./myhttp https://google.com
https://google.com 18f5d033a51b55d00454f76852515217
```

```
$ ./myhttp -parallel 3 google.com facebook.com yahoo.com yandex.com twitter.com reddit.com/r/funny reddit.com/r/notfunny
http://google.com 62d4461b476b946fbfba5b64b0860068
http://facebook.com cb7a949c553af8b91747ed6a0cc0f2d9
http://yandex.com 9aeab33dfb4e06d4e879dea0e7032e17
http://twitter.com 96e5326a4d43b7011b8d987025e948bd
http://reddit.com/r/funny 57cfa8fde5f882f0575b43d1a9b12550
http://yahoo.com b1ea7fcd5681c3226c000d51f72e3d42
http://reddit.com/r/notfunny 977cceb0949b792fe067a9e7cb3c86c0
```