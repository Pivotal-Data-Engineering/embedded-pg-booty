<h1> VMware has ended active development of this project, this repository will no longer be updated.</h1><br># A simple example to illustrate using an embedded PostgreSQL with a Spring Boot REST app

## How to build and deploy to Cloud Foundry
* `./gradlew clean build`
* `cf push # You may want to tweak manifest.yml first`
* Once this finishes, if you `cf ssh ...` to the app, you can see the PostgreSQL binary
  that was downloaded:
  ```
  vcap@b57e56ea-5b77-422e-4cfe-bce5:~$ ls -l $PWD/.embedpostgresql/postgresql-9.6.11-1-linux-x64-binaries.tar.gz
  -rw-r--r-- 1 vcap vcap 140462712 Jan 10 20:49 /home/vcap/.embedpostgresql/postgresql-9.6.11-1-linux-x64-binaries.tar.gz
  ```

## Test it out
Caveat: it doesn't do much, so far.  All it does now is print a simple greeting, but with
the addition of a timestamp string, where the timestamp is fetched from PostgreSQL, using
the `now()` function.  But, you can:
```
$ time curl http://embed-pg-test.YOUR_CF_INSTANCE.COM/hello-world?name=Mike
{"id":8693,"content":"Hello, Mike (2019-01-11 00:14:02.77967)!"}
real	0m0.056s
user	0m0.011s
sys	0m0.008s
```
Hopefully, we'll think of something even more clever to use this for.

## This is based almost entirely on the following two projects:
* [Spring Actuator Service Example](https://spring.io/guides/gs/actuator-service/)
* [Embedded PostgreSQL](https://github.com/yandex-qatools/postgresql-embedded)

## TODO
* It looks like most of the downloaded archive is pgAdmin, so it would be good to remove that:
```
pgsql$ du -sh *
9.5M	bin
19M	doc
9.2M	include
19M	lib
281M	pgAdmin 4
7.1M	share
8.7M	stackbuilder
```
* Here's where the download site is defined in the current code:
```
public PostgresDownloadConfigBuilder defaultsForCommand(Command command) {
        fileNaming().setDefault(new UUIDTempNaming());
        // I've found the only open and easy to use cross platform binaries
        downloadPath().setDefault(new DownloadPath("http://get.enterprisedb.com/postgresql/"));
```

