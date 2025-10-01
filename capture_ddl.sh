#!/bin/bash
# JPA가 생성하는 DDL만 캡처하는 스크립트

./gradlew bootRun --args='--spring.profiles.active=local' 2>&1 | while IFS= read -r line; do
    echo "$line"
    if [[ $line == *"create table subsidies"* ]]; then
        # subsidies 테이블 생성 DDL을 찾으면 다음 15줄 더 출력
        for i in {1..15}; do
            IFS= read -r line
            echo "$line"
        done
        break
    fi
done