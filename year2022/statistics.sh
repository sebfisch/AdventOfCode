#!/usr/bin/bash

cat <<EOF

## Usage Statistics

### Recent Extensions

| Count | Extension |
| ----: | --------- |
EOF

find . -name "*.java" \
    | xargs grep -h JEP \
    | sed 's/://g' \
    | sort -n \
    | uniq -c \
    | sort -g -k 4 \
    | sort -r -s -g -k 1 \
    | awk '{name=$5; for(i=6;i<=NF;i++){name=name " " $i}; printf("| %d | [%s](https://openjdk.org/jeps/%d) |\n", $1, name, $4)}'

cat <<EOF

### Imports

| Count | Import |
| ----: | ------ |
EOF

find . -name "*.java" \
    | xargs grep -h -E "^import" \
    | sed 's/;//g' \
    | sort -n \
    | uniq -c \
    | sort -g -k 2 \
    | sort -r -s -g -k 1 \
    | awk '{printf("| %d | [%s](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/%s.html) |\n", $1, $3, gensub(/\./,"/","g",$3))}'
