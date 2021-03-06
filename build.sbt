import com.trueaccord.scalapb.compiler.Version.scalapbVersion

name := """prototest"""

organization := "co.tala"

version := "1.0.0"

scalaVersion := "2.12.2"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies ++= Seq(
  // For finding google/protobuf/descriptor.proto
  "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "com.amazonaws" % "aws-java-sdk" % "1.11.46"
)

