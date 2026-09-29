# Enforcement Hearing Gateway (API spec)

`api-cp-crime-results-enforcementgateway`

OpenAPI specification for the **CP ↔ Libra/GoB enforcement hearing gateway**. It documents the
contract used by the runtime service
[`service-cp-crime-results-enforcementgateway`](https://github.com/hmcts/service-cp-crime-results-enforcementgateway):

- **Hearing confirmation:** the `confirmedHearing` payload CP sends to Libra (GoB) when an enforcement
  case is allocated to a court hearing.
- **Hearing updates:** the same payload re-sent when an allocation is amended.
- **Hearing results (CIMD-4246):** `POST /hearingResulted`, called by
  `service-cp-crime-results-enforcementworkflow` when an enforcement case defendant offence is resulted.
  The gateway forwards the `HearingResultedRequest` to Libra (via APIM) and returns Libra's
  `HearingResultedResponse` unchanged: `400` for a request that breaks the contract, `502` when Libra/APIM
  fails, times out, or accepts with a reply that isn't a valid `HearingResultedResponse`.

The spec is published as a generated artefact (OpenAPI generator) and consumed by the service as a
dependency, following the established `api-cp-crime-*` / `service-cp-crime-*` pairing.

> Owned by the **cp-case-ingestion-and-material** team. Created from the HMCTS template
> [`api-hmcts-crime-template`](https://github.com/hmcts/api-hmcts-crime-template).

> ⚠️ **Draft.** `src/main/resources/openapi/openapi-spec.yml` carries a draft `confirmedHearing`
> contract, which must still be reconciled with Libra's `HearingConfirmedRequest`. The authoritative
> Libra-side endpoint contract is owned by Libra.

### Schemas copied from Libra

The `HearingResultedRequest` … `CtBankDetails` schemas are copied **verbatim** from the Libra Gateway
Hearing Event API v0.4.0 (see the header comment above `HearingResultedRequest` in the spec). Keep them in
sync with Libra and do not edit them independently. There are two local amendments, each marked
`LOCAL AMENDMENT` inline, which must be kept when re-copying:

1. `nowsDataRequest` is optional in `HearingResultedRequest` (agreed with GoB; expected in v0.5.0).
2. `maxLength` is removed from enum-typed properties: Bean Validation's `@Size` cannot validate an enum
   (HV000030, a 500 on every request). `OpenApiObjectsTest` fails if a re-copy brings one back.

## Naming

Follows the HMCTS api-template convention `api-{source}-[case-type]-{business-domain}-{entity}`:
`cp` · `crime` · **`results`** · `enforcementgateway` — placing it in the results /
enforcement-integration domain. Owned by the **cp-case-ingestion-and-material** team.

> Note: `results` is not currently in the documented `business-domain` enumeration in
> `api-hmcts-crime-template` (`caseingestion` · `casematerial` · `caseadmin` · `casehearing` ·
> `schedulingandlisting`); confirm with the API governance owners if strict conformance is required.

## Build

```bash
gradle build      # validates + generates from src/main/resources/openapi/openapi-spec.yml
```

OpenAPI linting runs in CI (`.github/workflows/lint-openapi.yml`); spec evolution rules are in
[`docs/OPENAPI-SPEC-VERSIONING.md`](./docs/OPENAPI-SPEC-VERSIONING.md) and
[`docs/API-VERSIONING-STRATEGY.md`](./docs/API-VERSIONING-STRATEGY.md).

## CI/CD

- `ci-draft.yml` — validate/lint on PRs.
- `ci-released.yml` — on a **published GitHub Release**, publish the versioned spec artefact.
- `code-analysis.yml`, `codeql.yml`, `lint-openapi.yml`, `secrets-scanner.yml`, `auto-merge-dependabot.yml`.

`main` and `team/*` branches are protected and require at least one approving review.

## License

MIT — see [LICENSE](LICENSE).
