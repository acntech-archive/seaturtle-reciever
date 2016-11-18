package no.acntech.seaturtle.receiver.domain.hateoas;

import no.acntech.seaturtle.receiver.domain.Link;

import java.util.List;

public class HateoasObject {

    protected List<Link> links;

    public HateoasObject() {
    }

    public HateoasObject(List<Link> links) {
        this.links = links;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
