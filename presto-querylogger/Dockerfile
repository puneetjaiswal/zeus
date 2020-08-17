FROM lyft/java8:e28fcd8af40bb682c6fdddd3032fbdf6a4585da9
ARG IMAGE_VERSION
ENV IMAGE_VERSION=${IMAGE_VERSION}
COPY manifest.yaml /code/lyft-presto-querylog-plugin/manifest.yaml
COPY . /code/lyft-presto-querylog-plugin
WORKDIR /code/lyft-presto-querylog-plugin
RUN mvn -DskipTests package
