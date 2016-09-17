require 'spec_helper'
require 'timecop'
require 'request'

describe Request do
  def request
    Request.new('abc', {'c' => 3, 'a' => 1}, {'a' => 2})
  end

  describe '#initialize' do
    it 'merges defaults and parameters' do
      request.parameters.must_equal({'c' => 3, 'a' => 2})
    end
  end

  describe '#url' do
    it 'generates a request url' do
      t = Time.local(1990)

      Timecop.freeze(t) do
        request.url.must_equal("#{Request::BASE_URL}?a=2&c=3&timestamp=#{t.to_i}&hashkey=ee26ab7e7a1d9b578125daca515f2e3e4be1fe1e")
      end
    end
  end

  describe '#sort' do
    it 'sorts the parameters hash' do
      r = request
      r.sort
      r.parameters.to_a[0][0].must_equal('a')
    end
  end

  describe '#add_timestamp' do
    it 'adds a timestamp' do
      t = Time.now
      r = request

      Timecop.freeze(t) do
        r.add_timestamp
        r.parameters['timestamp'].must_equal(t.to_i)
      end
    end
  end

  describe '#concatenate' do
    it 'concatenates the parameters to a string' do
      request.concatenate.must_equal('c=3&a=2')
    end
  end

  describe '#add_hashkey' do
    it 'computes a sha1 hexdigest' do
      r = request
      r.sort
      r.add_hashkey

      r.parameters['hashkey'].must_equal('a6d21a72e8e88ca726f453f99c7113efcde61591')
    end
  end

  describe '#escape' do
    it 'url-encodes the parameters' do
      r = Request.new(nil, {}, {'a' => '&'})
      r.escape
      r.parameters['a'].must_equal('%26')
    end
  end
end
