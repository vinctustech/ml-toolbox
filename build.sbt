lazy val ml_toolbox = crossProject(JSPlatform, JVMPlatform, NativePlatform).in(file(".")).
  settings(
    name := "ml_toolbox",
    version := "0.1.0-snapshot.1",
    scalaVersion := "2.13.5",
    scalacOptions ++=
      Seq(
        "-deprecation", "-feature", "-unchecked",
        "-language:postfixOps", "-language:implicitConversions", "-language:existentials", "-language:dynamics",
        "-Xasync"
      ),
    organization := "com.vinctus",
    mainClass := Some("com.vinctus.ml_toolbox.Main"),
    Test / mainClass := Some("com.vinctus.ml_toolbox.Main"),
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.5" % "test",
    libraryDependencies += "xyz.hyperreal" %%% "cross-platform" % "0.1.0-snapshot.3",
    libraryDependencies ++=
      Seq(
        "xyz.hyperreal" %%% "importer" % "0.1.0-snapshot.1",
        "xyz.hyperreal" %%% "table" % "1.0.0-snapshot.3",
        "xyz.hyperreal" %%% "matrix" % "0.1.0-snapshot.5",
        "xyz.hyperreal" %%% "csv" % "0.1.1",
      ),
      publishMavenStyle := true,
    publishArtifact in Test := false,
    licenses += "ISC" -> url("https://opensource.org/licenses/ISC")
  ).
  jvmSettings(
    libraryDependencies += "org.scala-js" %% "scalajs-stubs" % "1.0.0" % "provided",
    libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.13" % "3.0.0"
  ).
  nativeSettings(
    nativeLinkStubs := true
  ).
  jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0",
    jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
    Test / scalaJSUseMainModuleInitializer := true,
    Test / scalaJSUseTestModuleInitializer := false,
//    Test / scalaJSUseMainModuleInitializer := false,
//    Test / scalaJSUseTestModuleInitializer := true,
    scalaJSUseMainModuleInitializer := true,
  )
