#!/bin/bash
PORTS=(9991 9981 9971)
IMAGE=biuqu/bq-auth:1.

sh build.sh "${PORTS[@]}" "$IMAGE"