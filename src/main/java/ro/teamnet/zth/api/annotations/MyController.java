package ro.teamnet.zth.api.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Alexandru.Negoita on 7/20/2017.
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface MyController {
    String urlPath();
}
