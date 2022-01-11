#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/app
PROJECT_NAME=g-app

echo "> Build file copy.."
echo "> $REPOSITORY/deploy/*.jar $REPOSITORY/"

cp $REPOSITORY/deploy/*.jar $REPOSITORY/

echo "> Deploy new application.."
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> Jar name : $JAR_NAME"
echo "> add permission.."

chmod +x $JAR_NAME

echo "> $JAR_NAME run.."

IDLE_PROFILE=$(find_idle_profile)

echo "> Env: $IDLE_PROFILE.."
echo "> java -jar -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &"

nohup java -jar \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

