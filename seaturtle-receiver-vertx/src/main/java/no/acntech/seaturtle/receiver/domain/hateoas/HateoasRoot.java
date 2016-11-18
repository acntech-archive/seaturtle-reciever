package no.acntech.seaturtle.receiver.domain.hateoas;

import no.acntech.seaturtle.receiver.domain.Link;

import java.util.List;

public class HateoasRoot extends HateoasObject {

    private Object content;

    public HateoasRoot() {
    }

    public HateoasRoot(Object content, List<Link> links) {
        super(links);
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
