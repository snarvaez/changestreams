package utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineOptions {

  boolean helpOnly = false;
  String mongoUri = "mongodb://127.0.0.1:27017,127.0.0.1:27018,127.0.0.1:27019";

  public CommandLineOptions(String[] args) throws ParseException {

    CommandLineParser parser = new GnuParser();

    Options cliOpts;
    cliOpts = new Options();

    cliOpts.addOption("c","host",true,"Mongodb connection details (default 'mongodb://127.0.0.1:27017,127.0.0.1:27018,127.0.0.1:27019' )");
    cliOpts.addOption("h","help",false,"Show Help");

    CommandLine cmd = parser.parse(cliOpts, args);

    // automatically generate the help statement
    if (cmd.hasOption("h"))
    {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "changestreams", cliOpts );
      helpOnly = true;
    }

    if (cmd.hasOption("c"))
    {
      mongoUri = cmd.getOptionValue("c");
    }

  }

  public String getMongoUri() {
    return mongoUri;
  }

  public boolean isHelpOnly() {
    return helpOnly;
  }

}
