package cassandra


import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import cassandra.CassandraConnector.cassandraKeySpace
import cassandra.CassandraConnector.usingCassandra


object ConnectorTest {

  def main(args: Array[String]): Unit =
  /**
   * The function '''usingCassandra''' is an utility that:
   *   - creates a Cassandra cluster with one node in docker
   *   - creates a Cassandra session to interact with the cluster
   *   - provides the session
   */
    usingCassandra { session =>
      {

        /**
         * The beginning of a Cassandra journey is creating a keyspace.
         * Since we only have one node, we ask the system to replicate
         * the data only once. The keyspace will be used then to create
         * tables.
         */
        val keyspaceQuery =
          SchemaBuilder
            .createKeyspace(cassandraKeySpace)
            .ifNotExists
            .withSimpleStrategy(1)

        session.execute(keyspaceQuery.build)

        /**
         * We can use the newly created keyspace by default. Here, we
         * use CQL. CQL stands for ''Cassandra Query Language''. It is a
         * SQL-like language designed to interact with Cassandra.
         */
        CqlIdentifier.fromCql(cassandraKeySpace)
      }
    }

}
