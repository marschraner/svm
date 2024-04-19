package ch.metzenthin.svm.domain.commands;

import org.docx4j.model.structure.PageDimensions;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.SectPr;

import java.math.BigInteger;

/**
 * @author Martin Schraner
 */
public class SetWordPageMarginsCommand implements Command {

    // input
    private final WordprocessingMLPackage wordMLPackage;
    private final ObjectFactory objectFactory;
    private final int top;
    private final int bottom;
    private final int left;
    private final int right;

    SetWordPageMarginsCommand(WordprocessingMLPackage wordMLPackage, ObjectFactory objectFactory, int top, int bottom, int left, int right) {
        this.wordMLPackage = wordMLPackage;
        this.objectFactory = objectFactory;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Override
    public void execute() {
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        Body body;
        try {
            body = mainDocumentPart.getContents().getBody();
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
        PageDimensions page = new PageDimensions();
        SectPr.PgMar pgMar = page.getPgMar();
        if (top > 0) {   // default: 720
            pgMar.setTop(BigInteger.valueOf(top));
        }
        if (bottom > 0) {  // default: 720
            pgMar.setBottom(BigInteger.valueOf(bottom));
        }
        if (left > 0) {  // default: 720
            pgMar.setLeft(BigInteger.valueOf(left));
        }
        if (right > 0) {  // default: 720
            pgMar.setRight(BigInteger.valueOf(right));
        }
        SectPr sectPr = objectFactory.createSectPr();
        body.setSectPr(sectPr);
        sectPr.setPgMar(pgMar);
    }

}
