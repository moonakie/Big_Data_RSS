import cassandra.CassandraConnector.{cassandraHost, cassandraPorts, dataCenter}
import com.datastax.oss.driver.api.core.CqlSession
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

import java.net.InetSocketAddress

def connect(): CqlSession = {
  val builder = CqlSession.builder()
  val session = builder.build()
  return session
}

