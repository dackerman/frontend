The Parameterized Build API is currently available as an early-access preview.
The API and functionality may change based on what we learn from your feedback.

The Parameterized Build API allows you to trigger a build using the
[CircleCI API](/docs/api)
and inject environment variables which are made available within the containers that run the build.

These environment variables can then be used to influence the steps that are run during the build.

For instance, you may have a build with a deploy step to a staging environment:

*   Once the deployment to staging is over you'd like to run functional tests against it.
*   You even have a build configured which can run your functional tests but you want to be able to run that build against different target hosts.

Build parameters allow you to use your functional test build against different targets on each run.

The current implementation is intentionally basic, the idea is to find out what you build and how you would like to build it so we can iterate quickly before stabilizing the API.
As such, either the API or how parameters are exposed during a build could change during the early-access period.
We'll communicate any changes before rolling them out.

Note: all the examples use `bash` and `curl` but there is nothing to stop you using your favourite HTTP library in your language of choice.

### Triggering Parameterized Builds

Builds are triggered by POSTing to [the trigger new build API](/docs/api#new-build).

A POST with an empty body will trigger a new build of the named branch.
You can include build parameters by sending a JSON body with `Content-type: application/json`:

```
{
  "build_parameters": {
    "param1": "value1",
    "param2": 500
  }
}
```

E.g. using `curl`

```
curl \
  --header "Content-Type: application/json" \
  --data '{"build_parameters": {"param1": "value1", "param2": 500}}' \
  --request POST \
  https://circleci.com/api/v1/project/circleci/mongofinil/tree/master?circle-token=$CIRCLE_TOKEN
```

The build will see the environment variables:

```
export param1="value1"
export param2="500"
```

### Constraints

The order that build parameters are loaded in isn't guaranteed so you shouldn't interpolate one build parameter into another.
It's best to think of build parameters as an unordered list of independent environment variables.

Since build parameters are just environment variables their names have to meet the following restrictions:

1.  They must contain only ASCII letters, digits and the underscore character.
2.  They must not begin with a number.
3.  They must contain at least one character.

Aside from the usual constraints for environment variables there aren't any restrictions on the values themselves, they're all just treated as simple strings at the moment.

E.g. if you passed the parameters:

```
{
  "build_parameters": {
    "foo": "bar",
    "baz": 5,
    "qux": {"quux": 1},
    "list": ["a", "list", "of", "strings"]
  }
}
```

Then your build will see the environment variables:

```
export foo="bar"
export baz="5"
export qux="{\"quux\": 1}"
export list="[\"a\", \"list\", \"of\", \"strings\"]"
```

Build parameters are exported as environment variables inside the build's containers and can be used by scripts/programs and commands in `circle.yml`.

### Example

We've put together a [how to](/docs/nightly-builds)
showing how to use build parameters in `circle.yml` so you can trigger nightly builds.

Obviously this is just one example of what you could do.
We're very interested in what you create with parameterized builds and would love to get your
[feedback](mailto:sayhi@circleci.com).
