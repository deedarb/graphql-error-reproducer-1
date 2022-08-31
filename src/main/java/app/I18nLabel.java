package app;

import io.smallrye.graphql.api.AdaptToScalar;
import io.smallrye.graphql.api.Scalar;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.impl.HeaderParser;
import io.vertx.ext.web.impl.ParsableLanguageValue;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.List;
import java.util.Optional;

@AdaptToScalar(Scalar.String.class)
public class I18nLabel {
    private String en;
    private String ru;

    public static I18nLabel fromString(String str) {
        return new JsonObject(str).mapTo(I18nLabel.class);
    }

    public static I18nLabel of(String en, String ru) {
        return new I18nLabel().setEn(en).setRu(ru);
    }

    @Override
    public String toString() {
        String lang = Optional.ofNullable(CDI.current())
            .map(cdi -> cdi.select(HttpServerRequest.class))
            .map(Instance::get)
            .flatMap(I18nLabel::extractLanguage).orElse("en");
        return switch (lang) {
            case "en" -> this.en;
            case "ru" -> this.ru;
            default -> this.en;
        };
    }

    public static Optional<String> extractLanguage(HttpServerRequest request) {
        String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        List<ParsableLanguageValue> languages = HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptLanguage, ParsableLanguageValue::new));
        return languages.stream()
            .map(ParsableLanguageValue::tag)
            .findFirst();
    }

    public String getEn() {
        return en;
    }

    public I18nLabel setEn(String en) {
        this.en = en;
        return this;
    }

    public String getRu() {
        return ru;
    }

    public I18nLabel setRu(String ru) {
        this.ru = ru;
        return this;
    }
}
