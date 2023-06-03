#!/bin/bash
#echo "s0'=$#"
#echo "s0=$0"
#echo "s1=$1"
#echo "s2=$2"
#echo "s3=$3"
#echo "s4=$4"

#构建指定端口的单个镜像
function build(){
  IMAGE=$1
  PORT=$2
  echo "image[$IMAGE]port:$PORT"
  HOST=$(ps -ef | grep -i docker | grep -i  '\-\-host\-ip' |awk -F 'host-ip' '{print $2}' | awk -F '--lowest-ip' '{print $1}')
  HOST=$(echo $HOST| awk '{gsub(/^\s+|\s+$/, "");print}')
  echo "${HOST}"
  echo ${#HOST}

  chmod  +x ./**
  SERVICE_DIR=$(find ./target -name "*.jar")
  SERVICE=$(basename "$SERVICE_DIR")
  echo "$SERVICE"

  SERVICE_NAME=$(basename "$SERVICE" .jar)
  echo "$SERVICE_NAME"
  docker build -t "${IMAGE}" -f ./Dockerfile ../ --build-arg HOST="${HOST}" --build-arg PORT="${PORT}" --build-arg SERVICE="${SERVICE}" --build-arg SERVICE_NAME="${SERVICE_NAME}"
}

#获取最后1个参数
IMAGE_PREFIX=${!#}
#获取参数列表
Params=$@
#获取参数个数
NUM="$#"
echo "parameter list:$Params"
echo "parameter num:$NUM"
echo "image prefix:$IMAGE_PREFIX"

if [ $NUM -gt 0 ]
then
  echo "start to build clusters."
  index=0
  #遍历参数列表
  for port in "$@"
  do
    #判断是否为数字端口
    num=`echo $port|sed 's/[0-9]//g'`
    #获取遍历的序号
    index=$((index+1))
  #  echo "index=$index"
    if [ -z "$num" ]
    then
  #    echo "$port is num"
      PORT=$port
      IMAGE=$IMAGE_PREFIX$index
      build "$IMAGE" "$PORT";
    fi
  done
else
  echo "start to build standalone."
  PORT=9991
  IMAGE="biuqu/bq-auth:1.0"
  build "$IMAGE" "$PORT";
fi

