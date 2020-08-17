export ORCA_DISABLED=true
SHELL := /bin/bash

ifneq ("$(wildcard /srv/repos/ops/base.mk)","")
include /srv/repos/ops/base.mk
else ifneq ("$(wildcard ../ops/base.mk)","")
include ../ops/base.mk
else
$(error Cannot find base.mk)
endif

SERVICE_NAME=lyftprestoquerylogplugin

.PHONY: release_staging
release_staging:
	true

.PHONY: release_development
release_development:
	echo "About to deploy to maven artifact repo"
	mvn -B -DskipTests deploy
	echo "Done deploying to maven artifact repo"

.PHONY: deploy
deploy:
	# do nothing, needed to please ci pipeline