package uk.gov.hmcts.cp.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.cp.openapi.api.EnforcementHearingApi;
import uk.gov.hmcts.cp.openapi.model.ConfirmedHearing;
import uk.gov.hmcts.cp.openapi.model.ErrorResponse;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class OpenApiObjectsTest {
    @Test
    void generated_error_response_should_have_expected_fields() {
        assertThat(ErrorResponse.class).hasDeclaredMethods("error", "message", "details", "traceId");
    }

    @Test
    void generated_error_response_timestamp_should_be_instant() throws Exception {
        Field timestampField = ErrorResponse.class.getDeclaredField("timestamp");

        assertThat(timestampField.getType())
                .as("timestamp field type")
                .isEqualTo(Instant.class);
    }

    @Test
    void generated_confirmed_hearing_should_have_expected_fields() {
        assertThat(ConfirmedHearing.class)
                .hasDeclaredFields("caseUrn", "courtHearingLocation", "dateOfHearing", "timeOfHearing");
    }

    @Test
    void generated_confirmed_hearing_date_of_hearing_should_be_local_date() throws Exception {
        Field dateOfHearingField = ConfirmedHearing.class.getDeclaredField("dateOfHearing");

        assertThat(dateOfHearingField.getType())
                .as("dateOfHearing field type")
                .isEqualTo(LocalDate.class);
    }

    @Test
    void generated_enforcement_hearing_api_should_have_expected_methods() {
        assertThat(EnforcementHearingApi.class).hasDeclaredMethods("postConfirmedHearing");
        assertThat(EnforcementHearingApi.PATH_POST_CONFIRMED_HEARING).isEqualTo("/confirmedHearing");
    }
}
