package mn.issue;

import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

import javax.inject.Inject;

@Controller("/dl")
public class DownloadController {

    @Client("alphabet")
    @Inject
    RxStreamingHttpClient alphabet;

    @Get(value = "/10k", produces = "application/pdf")
    public Flowable<ByteBuffer<?>> getBunny() {
        return alphabet.dataStream(HttpRequest.GET("/investor/static/pdf/20210203_alphabet_10K.pdf"));
    }
}
