require 'simplecov'

SimpleCov.start do
  add_filter '/spec/'
  add_filter '/config/'
end

require 'minitest/autorun'
require 'minitest/spec'

alias :context :describe

def setup_vcr!
  require 'vcr'

  VCR.configure do |config|
    config.cassette_library_dir = File.join(File.dirname(__FILE__), 'fixtures', 'vcr')
    config.hook_into :webmock
    config.default_cassette_options = {
      :match_requests_on => [
        :method,
        VCR.request_matchers.uri_without_param(:hashkey, :timestamp)
      ]
    }
  end
end
