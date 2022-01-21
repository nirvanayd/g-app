#!/usr/bin/env bash

function find_idle_profile()
{
    IDLE_PROFILE="prod"
    # bash script는 값의 반환이 안된다.
    # echo로 결과 출력 후, 그 값을 잡아서 사용한다.
    echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
    echo "8090"
}

function find_current_port()
{
        # curl 결과로 연결할 서비스 결정
        RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/prod-profile)

        if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
        then
            CURRENT_PROFILE="prod-green"
        else
            CURRENT_PROFILE=$(curl -s http://localhost/prod-profile)
        fi

        if [ "${CURRENT_PROFILE}" == "prod-blue" ]
        then
            echo "8081"
        else
            echo "8082"
        fi
}

