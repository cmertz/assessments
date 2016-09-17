require 'spec_helper'

require 'response'
require 'request'
require 'parameter_defaults'
require 'httparty'

setup_vcr!

describe Response do
  def response
    url = Request.new(nil, PARAMETER_DEFAULTS, {'uid' => 'player1'}).url
    Response.new(HTTParty.get(url))
  end

  describe '#error?' do
    it 'returns true on error' do
      VCR.use_cassette('invalid_hashkey') do
        response.error?.must_equal(true)
      end
    end
  end

  describe '#no_content?' do
    context 'content' do
      it 'returns false' do
        VCR.use_cassette('content') do
          response.no_content?.must_equal(false)
        end
      end
    end

    context 'no content' do
      it 'returns true' do
        VCR.use_cassette('no_content') do
          response.no_content?.must_equal(true)
        end
      end

      it 'is not an error' do
        VCR.use_cassette('no_content') do
          response.error?.must_equal(false)
        end
      end
    end
  end
end
