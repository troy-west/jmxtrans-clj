all: jar-maven

.PHONY: jar
jar: project.clj src/**/* resources/**/*
	lein -U do clean, deps, jar

.PHONY: jar-maven
jar-maven: jar
	lein update-in : assoc :repositories "{\"snapshots\" {:url \"s3p://$SNAPSHOTS_BUCKET\"}}" -- update-in :plugins conj '[s3-wagon-private "1.3.1"]' -- deploy snapshots
