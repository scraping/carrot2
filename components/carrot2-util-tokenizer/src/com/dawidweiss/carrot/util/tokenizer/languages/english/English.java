
/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2006, Dawid Weiss, Stanisław Osiński.
 * Portions (C) Contributors listed in "carrot2.CONTRIBUTORS" file.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * http://www.carrot2.org/carrot2.LICENSE
 */

package com.dawidweiss.carrot.util.tokenizer.languages.english;

import java.io.*;
import java.util.*;

import com.dawidweiss.carrot.core.local.linguistic.*;
import com.dawidweiss.carrot.filter.snowball.*;
import com.dawidweiss.carrot.util.tokenizer.languages.*;
import com.dawidweiss.carrot.util.tokenizer.parser.*;
import com.stachoodev.util.common.*;

/**
 * An implementation of {@link Language} interface
 * for the English language.
 * 
 * <p>Requires <code>carrot2-stemmer-snowball</code> for
 * stemming capabilities
 * 
 * @author Dawid Weiss
 * @version $Revision$
 */
public class English extends StemmedLanguageBase {

    /**
     * A set of stopwords for this language.
     */
    private final static Set stopwords;
    
    /*
     * Load stopwords from an associated resource.
     */
    static {
        String resourceName = "/com/dawidweiss/carrot/util/tokenizer/languages/english/stopwords.en";
        try {
			stopwords = WordLoadingUtils.loadWordSet(
                    resourceName, English.class.getResourceAsStream(resourceName));
		} catch (IOException e) {
            throw new RuntimeException("Could not load the required" +
                    "resource: " + resourceName);
		}
    }

    
    /**
     * Public constructor. 
     */
    public English() {
        super.setStopwords(stopwords);
    }
    
	/**
     * Creates a new instance of a {@link LanguageTokenizer} for 
     * this language.
     * 
	 * @see com.dawidweiss.carrot.util.tokenizer.languages.StemmedLanguageBase#createTokenizerInstanceInternal()
	 */
	protected LanguageTokenizer createTokenizerInstanceInternal() {
        // TODO: This tokenizer is never returned to the pool, but actually
        // all languages could share the same tokenizer pool
        return WordBasedParserFactory.Default.borrowParser();
	}

	/**
     * @return Language code: <code>en</code>
	 * @see com.dawidweiss.carrot.core.local.linguistic.Language#getIsoCode()
	 */
	public String getIsoCode() {
        return "en";
	}

    /** 
     * Return an instance of a stemmer for this language
     * from the Snowball component.
     *  
     * @see com.dawidweiss.carrot.util.tokenizer.languages.LanguageBase#createStemmerInstance()
     */
    protected Stemmer createStemmerInstance() {
        return SnowballStemmersFactory.getInstance(getIsoCode());
    }
}
