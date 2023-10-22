echo What is the version of OLog to bundle?
read version
echo Bundling $version...
cd target
jar -cvf olog-$version-bundle.jar olog-$version.pom olog-$version.pom.asc olog-$version.jar olog-$version.jar.asc olog-$version-javadoc.jar olog-$version-javadoc.jar.asc olog-$version-sources.jar olog-$version-sources.jar.asc
