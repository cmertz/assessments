# ruby developer challenge

## thoughts

* parameter validation in the Url class is omitted since the API should
  already validate them and this would only duplicate checks and make
  the implementation brittle in case of API changes
* the api key is sensible and thus not in the VCS
* http testing is mocked with VCR
* i suppose styling of the views is not the scope of this challenge
* deployment is done as a Docker container
