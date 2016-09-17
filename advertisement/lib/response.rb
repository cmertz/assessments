require 'json'

class Response
  CODE_NO_CONTENT = 'NO_CONTENT'

  def initialize(resp)
    @code = resp.code
    @body = JSON.parse(resp.body)
  end

  def error?
    @code != 200
  end

  def no_content?
    code == CODE_NO_CONTENT
  end

  def code
    @body['code']
  end

  def message
    @body['message']
  end

  def offers
    @body['offers']
  end
end
