@Grab ('edu.stanford.nlp:stanford-corenlp:3.8.0')
@Grab ('edu.stanford.nlp:stanford-corenlp:3.8.0:models')
@Grab ('org.slf4j:slf4j-log4j12:1.7.10') // for warnings
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations
import edu.stanford.nlp.ie.regexp.RegexNERSequenceClassifier
import edu.stanford.nlp.pipeline.*
import edu.stanford.nlp.pipeline.RegexNERAnnotator
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.util.Triple
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation
import edu.stanford.nlp.dcoref.CorefChain
import edu.stanford.nlp.semgraph.SemanticGraph
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.ling.CoreAnnotations.TrueCaseAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TrueCaseTextAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.MentionsAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.EntityTypeAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation
import java.util.*
import java.io.Writer
import java.io.FileWriter
import java.io.PrintWriter
import java.io.ByteArrayOutputStream


String toTrueCase(String sentence) {
    prop = new Properties()
    prop.setProperty("annotators", "tokenize, ssplit, truecase")
    process = new StanfordCoreNLP(prop)
    trueCase = new Annotation(sentence)
    process.annotate(trueCase)
    result = trueCase.get(SentencesAnnotation)
    String truCaseSentence = new String()
    for (wordList in result) {
        for (word in wordList.get(TokensAnnotation)) {
            truCaseSentence = truCaseSentence + word.get(TrueCaseTextAnnotation) + " " 
        }
    }
    truCaseSentence = truCaseSentence.trim()
    return truCaseSentence
}

ArrayList NEExtractor (String sentence) {
    println(sentence)
    settings = new Properties()
    settings.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, regexner")
    process = new StanfordCoreNLP(settings)
    println("process")
    NEAnnotation = new Annotation(sentence)
    process.annotate(NEAnnotation)
    println("annotated")
    result = NEAnnotation.get(SentencesAnnotation)
    entityList = new ArrayList<>()
    for (wordList in result) {
        for (word in wordList.get(TokensAnnotation)) {
            if (word.get(NamedEntityTagAnnotation) != "O") {
                t = new Triple(String, String, String)
                t.setFirst(word.get(NamedEntityTagAnnotation))
                t.setSecond(word.get(TextAnnotation))
                t.setThird("---")
                entityList.add(t)
            }
        }
    }
    return entityList
}

text = "I want to ride my hermesm@gmail.com, Scott Kennedy and ...john lennon want to ride my bike"
text = toTrueCase(text)
println("Extracting")
println(NEExtractor(text))
// pipeline.prettyPrint(document, baos)
// println(baos.toString())
