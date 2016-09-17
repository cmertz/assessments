require 'digest'

class Request
  attr_reader :parameters

  BASE_URL     = 'http://api.fyber.com/feed/v1/offers.json'

  def initialize(api_key, defaults, params)
    @api_key    = api_key
    @parameters = defaults.merge(params)
  end

  def url
    add_timestamp
    sort
    add_hashkey
    escape

    "#{BASE_URL}?#{concatenate}"
  end

  def sort
    @parameters = Hash[parameters.sort]
  end

  def add_timestamp
    @parameters['timestamp'] = Time.now.to_i
  end

  def concatenate
    @parameters.map{|(k,v)| "#{k}=#{v}"}.join('&')
  end

  def add_hashkey
    @parameters['hashkey'] = Digest::SHA1.hexdigest(concatenate + "&#{@api_key}")
  end

  def escape
    @parameters = Hash[@parameters.map{|(k,v)| [k, URI::encode_www_form_component(v)]}]
  end
end
