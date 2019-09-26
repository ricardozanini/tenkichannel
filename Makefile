QUAY_NAMESPACE = ricardozanini
VERSION = 1.0.0

.PHONY: build-images
build-images:
	- make build-weather-image
	- make build-rain-image

.PHONY: build-weather-image
build-weather-image:
	@echo .......... Building Weather API Gateway Image .................
	s2i build . openjdk/openjdk-11-rhel8 weather-api-gateway quay.io/${QUAY_NAMESPACE}/weather-api-gateway:${VERSION} -e MAVEN_ARGS_APPEND="-pl weather-api-gateway -am" ARTIFACT_DIR="weather-api-gateway/target"
	@echo .......... Pushing to quay.io .................................
	docker tag quay.io/${QUAY_NAMESPACE}/weather-api-gateway:${VERSION} quay.io/${QUAY_NAMESPACE}/weather-api-gateway:latest
	docker push quay.io/${QUAY_NAMESPACE}/weather-api-gateway:${VERSION}
	docker push quay.io/${QUAY_NAMESPACE}/weather-api-gateway:latest
	@echo .......... Image Weather API Gateway successfully built .......

.PHONY: build-rain-image
build-rain-image:
	@echo .......... Building Rain Forecast Process Image ...............
	s2i build . openjdk/openjdk-11-rhel8 rain-forecast-process quay.io/${QUAY_NAMESPACE}/rain-forecast-process:${VERSION}  -e MAVEN_ARGS_APPEND="-pl rain-forecast-process -am" ARTIFACT_DIR="rain-forecast-process/target"
	@echo .......... Pushing to quay.io .................................
	docker tag quay.io/${QUAY_NAMESPACE}/rain-forecast-process:${VERSION} quay.io/${QUAY_NAMESPACE}/rain-forecast-process:latest
	docker push quay.io/${QUAY_NAMESPACE}/rain-forecast-process:${VERSION}
	docker push quay.io/${QUAY_NAMESPACE}/rain-forecast-process:latest
	@echo .......... Image Rain Forecast Process successfully built .....

