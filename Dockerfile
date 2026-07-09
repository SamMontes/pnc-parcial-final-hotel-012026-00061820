FROM ubuntu:latest
LABEL authors="sammo"

ENTRYPOINT ["top", "-b"]