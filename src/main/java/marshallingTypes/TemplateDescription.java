package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class TemplateDescription {

    @JacksonXmlProperty(localName = "ID_FT", isAttribute = true)
    public int templateId;

    @JacksonXmlProperty(localName = "INDEX_FT", isAttribute = true)
    public String FTIndex; //TODO ft?

    @JacksonXmlProperty(localName = "NAME_FT", isAttribute = true)
    public String templateName;

    @JacksonXmlProperty(localName = "OKUD_FT", isAttribute = true)
    public String OKUDTemplateCode;

    @JacksonXmlProperty(localName = "ID_ESNT", isAttribute = true)
    public int ESNTId;

    @JacksonXmlProperty(localName = "NAME_ESNT", isAttribute = true)
    public String ESNTName;

    @JacksonXmlProperty(localName = "ID_PT", isAttribute = true)
    public int periodId;

    @JacksonXmlProperty(localName = "NAME_PT", isAttribute = true)
    public String periodName;

    @JacksonXmlProperty(localName = "IS_HAVE_DETAIL", isAttribute = true)
    public Boolean isHaveDetail;

    @JacksonXmlProperty(localName = "VER_NUMBER", isAttribute = true)
    public int versionNumber;

    @JacksonXmlProperty(localName = "INDEX_TEMPLATE", isAttribute = true)
    public String templateIndex;

    @JacksonXmlProperty(localName = "NAME_DOC", isAttribute = true)
    public String docName;

    @JacksonXmlProperty(localName = "IS_PRIVATE", isAttribute = true)
    public Boolean isPrivate; //TODO ?

    @JacksonXmlProperty(localName = "FORM_TYPE", isAttribute = true)
    public String formType;

    @JacksonXmlProperty(localName = "PERIOD_TYPE", isAttribute = true)
    public String periodType;

    @JacksonXmlProperty(localName = "REPEAT_TYPE", isAttribute = true)
    public String repeatType;

    @JacksonXmlProperty(localName = "CODE_DET", isAttribute = true)
    public String detCode; //TODO ?


    @JacksonXmlElementWrapper(localName = "FORM_EXTATTR")
    @JacksonXmlProperty(localName = "row")
    public List<Respondent> respondents; //TODO: multiple respondents?
}
