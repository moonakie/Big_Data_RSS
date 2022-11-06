package cassandra

import com.datastax.oss.driver.api.core.CqlSession
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.GenericContainer

import scala.jdk.CollectionConverters.SeqHasAsJava

import java.net.InetSocketAddress

class CassandraContainer(dockerImageName: DockerImageName)
  extends GenericContainer[CassandraContainer](dockerImageName){}

object CassandraConnector{
  lazy val dataCenter: String = "datacenter";
  lazy val cassandraHost: String = "localhost";
  lazy val cassandraPorts: Seq[Int]  = "9042".toSeq.map(_.toInt)
  lazy val cassandraKeySpace: String = "projectRSS";

  def usingCassandra(f: CqlSession => Unit): Unit = {
    case class CassandraConfiguration(seed: String, port: Int)

    val dockerImageName = DockerImageName.parse("cassandra").withTag("4.0")
    val cassandraClusterName = "cassandra-cluster"

    val cassandraSeeds = cassandraPorts.indices.map(i => s"cassandra-node${i + 1}")


    val containers : Seq[CassandraContainer] =
      cassandraSeeds.zip(cassandraPorts).map { case (seed, port) =>
      new CassandraContainer(dockerImageName)
        .withCreateContainerCmdModifier(cmd => cmd.withName(seed))
        .withEnv("CASSANDRA_CLUSTER_NAME", cassandraClusterName)
        .withStartupTimeout(java.time.Duration.ofMinutes(10))
        .withExposedPorts(port)
      }


    containers.foreach(_.start())

    val builder = CqlSession.builder()
    builder.addContactPoints(
      containers
        .zip(cassandraPorts)
        .map{case (container, port) => new InetSocketAddress(cassandraHost, container.getMappedPort(port))}
        .asJava
    )

    builder.withLocalDatacenter(dataCenter)

    val session = builder.build()

    try f(session)
    finally {
      session.close()
      containers.foreach(_.close())
    }
  }
}