package org.fugerit.java.demo.venussamplecustomfunction.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.doc.freemarker.fun.FMFunHelper;

import java.util.List;
import java.util.Properties;

/*
 * Translate a word to Quenya if available, return the same word otherwise
 *
 * Quenya is the language spoken by the high elves in J.R.R. Tolkien's world (https://lotr.fandom.com/wiki/Quenya).
 */
public class QuenyaFun implements TemplateMethodModelEx {

    private static final Properties QUENYA = PropsIO.loadFromClassLoaderSafe( "config/quenya.properties" );

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        FMFunHelper.checkParameterNumber( arguments, 1 );
        String wordToTranslate = arguments.get( 0 ).toString();
        // if not found, default to the input word
        String output = QUENYA.getProperty( wordToTranslate, wordToTranslate );
        return new SimpleScalar(output);
    }

}
