package com.buschmais.jqassistant.commandline.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import com.buschmais.jqassistant.commandline.CliExecutionException;
import com.buschmais.jqassistant.core.plugin.api.PluginRepository;
import com.buschmais.jqassistant.core.store.impl.EmbeddedGraphStore;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServerTaskTest {

    @Mock
    PluginRepository pluginRepository;

    @Mock
    private EmbeddedGraphStore store;

    private ServerTask serverTask = new ServerTask();

    @Test
    public void daemon() throws CliExecutionException, ParseException {
        startServer("-daemon");
    }

    @Test
    public void standalone() throws CliExecutionException, ParseException {
        String data = "\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            startServer(new String[] {});
        } finally {
            System.setIn(stdin);

        }
    }

    private void startServer(String... arguments) throws ParseException, CliExecutionException {
        Options options = new Options();
        for (Option option : serverTask.getOptions()) {
            options.addOption(option);
        }
        CommandLineParser parser = new BasicParser();
        CommandLine commandLine = parser.parse(options, arguments);

        serverTask.initialize(pluginRepository, new HashMap<>());
        serverTask.withOptions(commandLine);
        serverTask.executeTask(store);
    }

}
