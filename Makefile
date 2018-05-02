all: jar-maven

.PHONY: jar
jar:
	lein -U do clean, deps, jar

.PHONY: jar-maven
jar-maven: jar
ifeq ($(SNAPSHOTS_BUCKET),)
	echo "SNAPSHOTS_BUCKET missing"
	exit 1
endif

	aws sts get-caller-identity

	#lein update-in :repositories conj "[\"snapshots\" {:url \"s3p://$(SNAPSHOTS_BUCKET)\" :no-auth true}]" -- update-in :plugins conj '[s3-wagon-private "1.3.1"]' -- deps

	lein update-in :repositories conj "[\"snapshots\" {:url \"s3p://$(SNAPSHOTS_BUCKET)\" :no-auth true}]" -- update-in :plugins conj '[s3-wagon-private "1.3.1"]' -- deploy snapshots
