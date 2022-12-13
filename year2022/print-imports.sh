#!/usr/bin/bash

cat <<EOF

## Import Counts

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
