/**
 * Copyright (c) 2014 Rudolf Schlatte. All rights reserved.
 * This file is licensed under the terms of the Modified BSD License.
 */
package frontend.antlr.parser;

import java.io.*;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

//import antlr.v4.runtime.tree.*;

import abs.frontend.antlr.parser.ABSLexer;
import abs.frontend.antlr.parser.ABSParser;
import abs.frontend.ast.CompilationUnit;

import abs.common.Constants;
import abs.frontend.ast.*;
import abs.frontend.parser.ASTPreProcessor;

public class ABSParserWrapper {

    boolean raiseExceptions = false;
    boolean stdlib = true;
    File file = null;

    public ABSParserWrapper () {}

    public ABSParserWrapper (File file, boolean raiseExceptions, boolean stdlib)
    {
        this.file = file;
        this.raiseExceptions = raiseExceptions;
        this.stdlib = stdlib;
    }

    public CompilationUnit parse(Reader reader) throws IOException {
        String path = "<unknown path>";
        if (file != null) path = file.getPath();
        SyntaxErrorCollector errorlistener
            = new SyntaxErrorCollector(file, raiseExceptions);
        ANTLRInputStream input = new ANTLRInputStream(reader);
        ABSLexer lexer
            = new abs.frontend.antlr.parser.ABSLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorlistener);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        abs.frontend.antlr.parser.ABSParser aparser
            = new abs.frontend.antlr.parser.ABSParser(tokens);
        aparser.removeErrorListeners();
        aparser.addErrorListener(errorlistener);
        ParseTree tree = aparser.goal();
        if (errorlistener.parserErrors.isEmpty()) {
        	System.out.println("Rest");
            ParseTreeWalker walker = new ParseTreeWalker();
            CreateJastAddASTListener l = new CreateJastAddASTListener(file);
            walker.walk(l, tree);
            CompilationUnit u
                = new ASTPreProcessor().preprocess(l.getCompilationUnit());
            if (stdlib) {
                for (ModuleDecl d : u.getModuleDecls()) {
                    if (!Constants.STDLIB_NAME.equals(d.getName()))
                        d.getImports().add(new StarImport(Constants.STDLIB_NAME));
                }
            }
            return u;
        } else {
            @SuppressWarnings("rawtypes")
                CompilationUnit u = new CompilationUnit(path,new List(),new List(),new List(),new Opt(),new List(),new List(),new List());
            u.setParserErrors(errorlistener.parserErrors);
            return u;
        }
    }
}
