# Wener with Java

## Common

> Common core utils for daily java development.

## Boot

> Spring Boot Scaffold

## Vaadin

> Vaadin utils & auto-configuration

## Redis

> Redis for fun

* Non-Blocking ByteBuf based packet reader/writer
* Normal redis server handler
* [ ] Redis command execution framework
* [ ] Redis command parser
* [ ] RDB codec
* [ ] PSYNC handler
* [ ] Cluster protocol codec

### Simple Server
* me.wener.wava.redis.example.RedisSimpleServer
* Backend by a HashMap per connection

```bash
redis-benchmark -t set,get -p 7890 -q --csv -l -c 10
```

Speed goes high (warm up), then down (gc).

```csv
SET,68027.21
GET,83056.48
SET,79239.30
GET,89525.52
SET,84674.01
GET,85034.02
SET,92081.03
GET,91074.68
SET,91407.68
GET,91407.68
SET,91911.76
GET,91659.03
SET,91827.37
GET,86880.97
SET,89525.52
GET,85836.91
SET,88339.23
GET,86956.52
SET,86058.52
GET,83263.95
SET,85689.80
GET,87032.20
SET,85910.65
GET,70472.16
SET,81632.65
GET,70372.98
SET,69930.07
GET,80321.28
```

```bash
redis-benchmark -t set,get -p 7890 -q --csv -l -c 10
redis-benchmark -t set,get -p 7890 -q --csv -l -c 10
```

About 120k max, CPU 200%

Last 15 record.

```csv
SET,57836.90,57670.13
GET,58616.65,60938.45
SET,60496.07,61087.36
GET,61050.06,60459.49
SET,61050.06,63291.14
GET,62073.25,60975.61
SET,61538.46,61957.87
GET,62305.30,63653.72
SET,63451.78,62073.25
GET,61881.19,62695.92
SET,61312.08,60975.61
GET,60277.27,60132.29
SET,61576.36,62266.50
GET,61881.19,61957.87
SET,61766.52,61996.28
GET,61199.51,60569.35
```

### Redis Server

```bash
redis-benchmark -t set,get -q --csv -l -c 10
```

Speed is very steady.

```csv
SET,91157.70
GET,92592.59
SET,92336.11
GET,91827.37
SET,90497.73
GET,92678.41
SET,93196.65
GET,92506.94
SET,92165.90
GET,92165.90
SET,90661.83
GET,91074.68
SET,90171.33
GET,92421.44
SET,91911.76
GET,90171.33
```

About 140k, CPU 100%

```csv
SET,71942.45,72411.30
GET,72306.58,72463.77
SET,71225.07,71530.76
GET,72098.05,71890.73
SET,71022.73,71123.76
GET,71633.23,71787.51
SET,71839.09,71377.59
GET,71942.45,72516.32
SET,71581.96,71787.51
GET,72939.46,71942.45
```

