require 'grape'

module Twitter
  class API < Grape::API

    version 'v1', using: :header, vendor: 'twitter'
    format :json

	resource :abc do
      desc "Return a public timeline."
      get :pavyzdys do
		{abc: 4}
      end
	end
  end
end
