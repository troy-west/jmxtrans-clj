all: jar-maven

.PHONY: jar
jar:
	lein -U do clean, deps, jar

.PHONY: jar-maven
jar-maven: jar
	lein update-in :repositories conj "[\"snapshots\" {:url \"s3p://$(SNAPSHOTS_BUCKET)\" :no-auth true}]" -- update-in :plugins conj '[s3-wagon-private "1.3.1"]' -- deploy snapshots
