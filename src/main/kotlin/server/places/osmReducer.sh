#!/usr/bin/env bash
cat ./$1| sed '/<node.*\/>/d' | tr \\n \# | sed -e 's/<way.*//g' | tr \# \\n > $1_r
echo "</osm>" >> $1_r